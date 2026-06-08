import { NextRequest, NextResponse } from "next/server";
import { getKeycloakClientId, getKeycloakTokenUrl } from "@/lib/env";

type LoginPayload = {
  username?: string;
  password?: string;
};

export async function POST(request: NextRequest) {
  let payload: LoginPayload;

  try {
    payload = (await request.json()) as LoginPayload;
  } catch {
    return NextResponse.json({ message: "Corpo JSON inválido para o login." }, { status: 400 });
  }

  if (!payload.username || !payload.password) {
    return NextResponse.json(
      { message: "Informe username e password para obter o token." },
      { status: 400 },
    );
  }

  const form = new URLSearchParams({
    client_id: getKeycloakClientId(),
    grant_type: "password",
    username: payload.username,
    password: payload.password,
  });

  const response = await fetch(getKeycloakTokenUrl(), {
    method: "POST",
    headers: {
      "Content-Type": "application/x-www-form-urlencoded",
      Accept: "application/json",
    },
    body: form,
    cache: "no-store",
  });

  const contentType = response.headers.get("content-type") ?? "";
  const data = contentType.includes("application/json")
    ? await response.json()
    : { message: await response.text() };

  if (!response.ok) {
    return NextResponse.json(
      {
        message:
          typeof data?.error_description === "string"
            ? data.error_description
            : "Keycloak recusou as credenciais informadas.",
      },
      { status: response.status },
    );
  }

  return NextResponse.json(data, { status: 200 });
}
