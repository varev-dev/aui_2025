export interface Node {
  id: string;
  hostname: string;
  gpuLoad: number;
  temperature: number;
  cluster: {
    id: string;
    name: string;
  };
}

export interface NodeList {
  id: string;
  hostname: string;
}

export interface CreateUpdateNode {
  hostname: string;
  gpuLoad: number;
  temperature: number;
}