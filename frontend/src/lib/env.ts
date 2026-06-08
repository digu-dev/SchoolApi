function ensureNoTrailingSlash(value: string) {
  return value.replace(/\/+$/, "");
}

export function getBackendApiUrl() {
  return ensureNoTrailingSlash(process.env.BACKEND_API_URL ?? "http://localhost:8080");
}

export function getKeycloakRealmUrl() {
  return ensureNoTrailingSlash(
    process.env.KEYCLOAK_REALM_URL ?? "http://localhost:8180/realms/school",
  );
}

export function getKeycloakTokenUrl() {
  return `${getKeycloakRealmUrl()}/protocol/openid-connect/token`;
}

export function getKeycloakClientId() {
  return process.env.KEYCLOAK_CLIENT_ID ?? "school-api";
}
