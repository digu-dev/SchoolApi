export type HttpMethod = "GET" | "POST" | "PUT" | "PATCH" | "DELETE";

export type TokenResponse = {
  access_token: string;
  expires_in: number;
  refresh_expires_in?: number;
  refresh_token?: string;
  scope?: string;
  token_type?: string;
  session_state?: string;
};

export type AuthProfile = {
  username: string;
  subject: string;
  issuer: string | null;
  issuedAt: string | null;
  expiresAt: string | null;
  authorities: string[];
  claims: Record<string, unknown>;
};

export type ApiResult<T> = {
  status: number;
  data: T;
  contentType: string;
};

export type EndpointParam = {
  name: string;
  label: string;
  placeholder: string;
};

export type EndpointAction = {
  id: string;
  label: string;
  method: HttpMethod;
  pathTemplate: string;
  description: string;
  pathParams?: EndpointParam[];
  queryParams?: EndpointParam[];
  sampleBody?: unknown;
};

export type ResourceDefinition = {
  slug: string;
  title: string;
  description: string;
  allowedRoles: string[];
  actions: EndpointAction[];
};

export type EndpointExecutionResult = {
  method: HttpMethod;
  path: string;
  status: number;
  payload: unknown;
};
