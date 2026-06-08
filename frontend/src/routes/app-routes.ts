import { resourceCatalog } from "@/lib/resource-config";

export const appRoutes = {
  dashboard: "/dashboard",
  login: "/login",
  profile: "/profile",
};

export function getResourceRoute(slug: string) {
  return `/resources/${slug}`;
}

export const navigationSections = [
  {
    title: "Visao geral",
    links: [
      {
        href: appRoutes.dashboard,
        label: "Dashboard",
        description: "Resumo de autenticacao, roles e atalhos.",
      },
      {
        href: appRoutes.profile,
        label: "Perfil",
        description: "Inspeciona claims, issuer e expiracao do token.",
      },
    ],
  },
  {
    title: "Recursos da API",
    links: resourceCatalog.map((resource) => ({
      href: getResourceRoute(resource.slug),
      label: resource.title,
      description: resource.description,
    })),
  },
];
