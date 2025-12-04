import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Cluster, ClusterList, CreateUpdateCluster } from '../models/cluster.model';

@Injectable({
  providedIn: 'root'
})
export class ClusterService {
  private apiUrl = 'http://localhost:8080/api/clusters';

  constructor(private http: HttpClient) {}

  // Get all clusters
  getAllClusters(): Observable<ClusterList[]> {
    return this.http.get<ClusterList[]>(this.apiUrl);
  }

  // Get single cluster by ID
  getCluster(id: string): Observable<Cluster> {
    return this.http.get<Cluster>(`${this.apiUrl}/${id}`);
  }

  // Create new cluster
  createCluster(cluster: CreateUpdateCluster): Observable<Cluster> {
    return this.http.post<Cluster>(this.apiUrl, cluster);
  }

  // Update existing cluster
  updateCluster(id: string, cluster: CreateUpdateCluster): Observable<Cluster> {
    return this.http.put<Cluster>(`${this.apiUrl}/${id}`, cluster);
  }

  // Delete cluster
  deleteCluster(id: string): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}