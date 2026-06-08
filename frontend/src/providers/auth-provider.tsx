"use client";

import {
  createContext,
  useCallback,
  useContext,
  useEffect,
  useMemo,
  useState,
} from "react";
import type { AuthProfile, TokenResponse } from "@/lib/types";

type Credentials = {
  username: string;
  password: string;
};

type AuthStatus = "loading" | "authenticated" | "anonymous";

type AuthContextValue = {
  status: AuthStatus;
  token: string | null;
  tokenResponse: TokenResponse | null;
  profile: AuthProfile | null;
  loginError: string | null;
  login: (credentials: Credentials) => Promise<void>;
  logout: () => void;
  refreshProfile: () => Promise<void>;
};

const STORAGE_KEY = "school-api-auth";
const AuthContext = createContext<AuthContextValue | null>(null);

function readStoredTokenResponse() {
  if (typeof window === "undefined") {
    return null;
  }

  const rawValue = window.localStorage.getItem(STORAGE_KEY);

  if (!rawValue) {
    return null;
  }

  try {
    const parsed = JSON.parse(rawValue) as TokenResponse;
    return parsed.access_token ? parsed : null;
  } catch {
    return null;
  }
}

async function parseJson<T>(response: Response): Promise<T | null> {
  const contentType = response.headers.get("content-type") ?? "";

  if (!contentType.includes("application/json")) {
    return null;
  }

  return (await response.json()) as T;
}

export function AuthProvider({ children }: { children: React.ReactNode }) {
  const [tokenResponse, setTokenResponse] = useState<TokenResponse | null>(() =>
    readStoredTokenResponse(),
  );
  const [status, setStatus] = useState<AuthStatus>(() => {
    if (typeof window === "undefined") {
      return "loading";
    }

    return readStoredTokenResponse() ? "loading" : "anonymous";
  });
  const [profile, setProfile] = useState<AuthProfile | null>(null);
  const [loginError, setLoginError] = useState<string | null>(null);

  const clearSession = useCallback(() => {
    localStorage.removeItem(STORAGE_KEY);
    setTokenResponse(null);
    setProfile(null);
    setLoginError(null);
    setStatus("anonymous");
  }, []);

  const fetchProfile = useCallback(
    async (accessToken: string) => {
      const response = await fetch("/api/auth/me", {
        headers: {
          Authorization: `Bearer ${accessToken}`,
        },
      });

      if (!response.ok) {
        const data = await parseJson<{ message?: string }>(response);
        throw new Error(data?.message ?? "Nao foi possivel validar o token atual.");
      }

      return (await response.json()) as AuthProfile;
    },
    [],
  );

  useEffect(() => {
    if (!tokenResponse?.access_token) {
      return;
    }

    let cancelled = false;

    fetchProfile(tokenResponse.access_token)
      .then((nextProfile) => {
        if (cancelled) {
          return;
        }

        setProfile(nextProfile);
        setStatus("authenticated");
      })
      .catch(() => {
        if (cancelled) {
          return;
        }

        clearSession();
      });

    return () => {
      cancelled = true;
    };
  }, [clearSession, fetchProfile, tokenResponse]);

  const login = useCallback(
    async (credentials: Credentials) => {
      setLoginError(null);

      const response = await fetch("/api/auth/login", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(credentials),
      });

      const data = await parseJson<TokenResponse & { message?: string }>(response);

      if (!response.ok || !data?.access_token) {
        throw new Error(data?.message ?? "Nao foi possivel obter um token do Keycloak.");
      }

      localStorage.setItem(STORAGE_KEY, JSON.stringify(data));
      setTokenResponse(data);
      setLoginError(null);
      const nextProfile = await fetchProfile(data.access_token);
      setProfile(nextProfile);
      setStatus("authenticated");
    },
    [fetchProfile],
  );

  const logout = useCallback(() => {
    clearSession();
  }, [clearSession]);

  const refreshProfile = useCallback(async () => {
    if (!tokenResponse) {
      clearSession();
      return;
    }

    const nextProfile = await fetchProfile(tokenResponse.access_token);
    setProfile(nextProfile);
    setStatus("authenticated");
  }, [clearSession, fetchProfile, tokenResponse]);

  const value = useMemo<AuthContextValue>(
    () => ({
      status,
      token: tokenResponse?.access_token ?? null,
      tokenResponse,
      profile,
      loginError,
      login: async (credentials) => {
        try {
          await login(credentials);
        } catch (error) {
          const message =
            error instanceof Error ? error.message : "Erro inesperado no login.";
          setLoginError(message);
          setStatus("anonymous");
          throw error;
        }
      },
      logout,
      refreshProfile,
    }),
    [login, loginError, logout, profile, refreshProfile, status, tokenResponse],
  );

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}

export function useAuthContext() {
  const context = useContext(AuthContext);

  if (!context) {
    throw new Error("useAuthContext precisa ser usado dentro de AuthProvider.");
  }

  return context;
}
