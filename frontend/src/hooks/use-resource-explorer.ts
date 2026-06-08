"use client";

import { useState } from "react";
import { useApi } from "@/hooks/use-api";
import type {
  ApiResult,
  EndpointAction,
  EndpointExecutionResult,
} from "@/lib/types";

type ParamValues = Record<string, string>;

function buildPath(template: string, values: ParamValues) {
  return template.replace(/\{([^}]+)\}/g, (_, key: string) => {
    const value = values[key]?.trim();

    if (!value) {
      throw new Error(`Preencha o campo "${key}" para montar a rota.`);
    }

    return encodeURIComponent(value);
  });
}

function filterValues(values: ParamValues) {
  return Object.fromEntries(
    Object.entries(values).filter(([, value]) => value.trim().length > 0),
  );
}

function stringifyBody(sample: EndpointAction["sampleBody"]) {
  if (sample === undefined) {
    return "";
  }

  return JSON.stringify(sample, null, 2);
}

export function useResourceExplorer(action: EndpointAction) {
  const apiRequest = useApi();
  const [pathValues, setPathValues] = useState<ParamValues>({});
  const [queryValues, setQueryValues] = useState<ParamValues>({});
  const [bodyValue, setBodyValue] = useState<string>(stringifyBody(action.sampleBody));
  const [pending, setPending] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [result, setResult] = useState<EndpointExecutionResult | null>(null);

  const hasBody = action.method === "POST" || action.method === "PUT" || action.method === "PATCH";

  const run = async () => {
    setPending(true);
    setError(null);

    try {
      const path = buildPath(action.pathTemplate, pathValues);
      const body = hasBody && bodyValue.trim().length > 0 ? JSON.parse(bodyValue) : undefined;
      const response = await apiRequest<ApiResult<unknown>["data"]>({
        method: action.method,
        path,
        query: filterValues(queryValues),
        body,
      });

      setResult({
        method: action.method,
        path,
        status: response.status,
        payload: response.data,
      });
    } catch (executionError) {
      setResult(null);
      setError(
        executionError instanceof Error
          ? executionError.message
          : "Nao foi possivel concluir a chamada.",
      );
    } finally {
      setPending(false);
    }
  };

  return {
    bodyValue,
    error,
    hasBody,
    pathValues,
    pending,
    queryValues,
    result,
    run,
    setBodyValue,
    setPathValues,
    setQueryValues,
  };
}
