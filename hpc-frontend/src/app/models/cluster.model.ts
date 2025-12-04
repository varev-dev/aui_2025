export interface Cluster {
  id: string;
  name: string;
  location: string;
}

export interface ClusterList {
  id: string;
  name: string;
}

export interface CreateUpdateCluster {
  name: string;
  location: string;
}