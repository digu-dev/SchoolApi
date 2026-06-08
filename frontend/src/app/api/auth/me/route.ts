import { NextRequest, NextResponse } from "next/server";
import { getBackendApiUrl } from "@/lib/env";

export async function GET(request: NextRequest) {
  const authorization = request.headers.get("authorization");

  if (!authorization) {
    return NextResponse.json({ message: "Authorization Bearer obrigatorio." }, { status: 401 });
  }

  const response = await fetch(`${getBackendApiUrl()}/auth/me`, {
    headers: {
      Authorization: authorization,
      Accept: "application/json",
    },
    cache: "no-store",
  });

  const contentType = response.headers.get("content-type") ?? "";
  const data = contentType.includes("application/json")
    ? await response.json()
    : { message: await response.text() };

  return NextResponse.json(data, { status: response.status });
}
