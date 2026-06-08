"use client";

import { useCallback } from "react";
import { useAuth } from "@/hooks/use-auth";
import type { ApiResult, HttpMethod } from "@/lib/types";

type ApiRequestArgs = {
  path: string;
  method?: HttpMethod;
  query?: Record<string, string>;
  body?: unknown;
};

function normalizePath(path: string) {
  return path.replace(/^\/+/, "");
}

function toReadableMessage(payload: unknown, fallback: string) {
  if (typeof payload === "string" && payload.trim()) {
    return payload;
  }

  if (payload && typeof payload === "object") {
    const objectPayload = payload as Record<string, unknown>;

    if (typeof objectPayload.message === "string") {
      return objectPayload.message;
    }

    if (typeof objectPayload.error === "string") {
      return objectPayload.error;
    }
  }

  return fallback;
}

export function useApi() {
  const { logout, token } = useAuth();

  return useCallback(
    async <T>({
      path,
      method = "GET",
      query,
      body,
    }: ApiRequestArgs): Promise<ApiResult<T>> => {
      if (!token) {
        throw new Error("Faca login antes de consultar a API.");
      }

      const searchParams = new URLSearchParams(query);
      const url = `/api/backend/${normalizePath(path)}${
        searchParams.size > 0 ? `?${searchParams.toString()}` : ""
      }`;

      const response = await fetch(url, {
        method,
        headers: {
          Authorization: `Bearer ${token}`,
          ...(body === undefined ? {} : { "Content-Type": "application/json" }),
        },
        body: body === undefined ? undefined : JSON.stringify(body),
      });

      const contentType = response.headers.get("content-type") ?? "";
      const payload = contentType.includes("application/json")
        ? await response.json()
        : await response.text();

      if (response.status === 401) {
        logout();
      }

      if (!response.ok) {
        throw new Error(
          toReadableMessage(payload, `Falha HTTP ${response.status} em ${method} ${path}.`),
        );
      }

      return {
        status: response.status,
        data: payload as T,
        contentType,
      };
    },
    [logout, token],
  );
}
