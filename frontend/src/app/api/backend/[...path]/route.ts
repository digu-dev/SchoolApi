import { NextRequest, NextResponse } from "next/server";
import { getBackendApiUrl } from "@/lib/env";

export const dynamic = "force-dynamic";

async function proxy(request: NextRequest, pathSegments: string[]) {
  const upstreamUrl = new URL(`${getBackendApiUrl()}/${pathSegments.join("/")}`);
  upstreamUrl.search = request.nextUrl.search;

  const headers = new Headers();
  const authorization = request.headers.get("authorization");
  const contentType = request.headers.get("content-type");
  const accept = request.headers.get("accept");

  if (authorization) {
    headers.set("authorization", authorization);
  }

  if (contentType) {
    headers.set("content-type", contentType);
  }

  if (accept) {
    headers.set("accept", accept);
  }

  const init: RequestInit = {
    method: request.method,
    headers,
    cache: "no-store",
  };

  if (!["GET", "HEAD"].includes(request.method)) {
    init.body = await request.text();
  }

  const response = await fetch(upstreamUrl, init);
  const body = await response.arrayBuffer();
  const responseHeaders = new Headers();
  const mirroredHeaders = ["content-type", "location"];

  for (const headerName of mirroredHeaders) {
    const headerValue = response.headers.get(headerName);

    if (headerValue) {
      responseHeaders.set(headerName, headerValue);
    }
  }

  return new NextResponse(body, {
    status: response.status,
    headers: responseHeaders,
  });
}

type BackendRouteContext = {
  params: Promise<{ path: string[] }>;
};

export async function GET(request: NextRequest, context: BackendRouteContext) {
  const { path } = await context.params;
  return proxy(request, path);
}

export async function POST(request: NextRequest, context: BackendRouteContext) {
  const { path } = await context.params;
  return proxy(request, path);
}

export async function PUT(request: NextRequest, context: BackendRouteContext) {
  const { path } = await context.params;
  return proxy(request, path);
}

export async function PATCH(request: NextRequest, context: BackendRouteContext) {
  const { path } = await context.params;
  return proxy(request, path);
}

export async function DELETE(request: NextRequest, context: BackendRouteContext) {
  const { path } = await context.params;
  return proxy(request, path);
}
