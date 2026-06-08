import { notFound } from "next/navigation";
import { ResourceExplorer } from "@/components/resource-explorer";
import { getResourceBySlug } from "@/lib/resource-config";

export default async function ResourcePage({
  params,
}: {
  params: Promise<{ resource: string }>;
}) {
  const { resource } = await params;
  const definition = getResourceBySlug(resource);

  if (!definition) {
    notFound();
  }

  return <ResourceExplorer resource={definition} />;
}
