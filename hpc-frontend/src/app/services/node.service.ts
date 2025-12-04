import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Node, NodeList, CreateUpdateNode } from '../models/node.model';

@Injectable({
  providedIn: 'root'
})
export class NodeService {
  private apiUrl = 'http://localhost:8080/api';

  constructor(private http: HttpClient) {}

  // Get all nodes from specific cluster
  getClusterNodes(clusterId: string): Observable<NodeList[]> {
    return this.http.get<NodeList[]>(`${this.apiUrl}/clusters/${clusterId}/nodes`);
  }

  // Get single node
  getNode(clusterId: string, nodeId: string): Observable<Node> {
    return this.http.get<Node>(`${this.apiUrl}/clusters/${clusterId}/nodes/${nodeId}`);
  }

  // Create new node in cluster
  createNode(clusterId: string, node: CreateUpdateNode): Observable<Node> {
    return this.http.post<Node>(`${this.apiUrl}/clusters/${clusterId}/nodes`, node);
  }

  // Update existing node
  updateNode(clusterId: string, nodeId: string, node: CreateUpdateNode): Observable<Node> {
    return this.http.put<Node>(`${this.apiUrl}/clusters/${clusterId}/nodes/${nodeId}`, node);
  }

  // Delete node
  deleteNode(clusterId: string, nodeId: string): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/clusters/${clusterId}/nodes/${nodeId}`);
  }
}