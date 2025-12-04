import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { ClusterService } from '../../services/cluster.service';
import { NodeService } from '../../services/node.service';
import { Cluster } from '../../models/cluster.model';
import { NodeList } from '../../models/node.model';

@Component({
  selector: 'app-cluster-details',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './cluster-details.html',
  styleUrl: './cluster-details.css'
})
export class ClusterDetailsComponent implements OnInit {
  cluster: Cluster | null = null;
  nodes: NodeList[] = [];
  clusterId: string = '';
  loading = false;
  error: string | null = null;

  constructor(
    private clusterService: ClusterService,
    private nodeService: NodeService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      this.clusterId = params.get('id') || '';
      if (this.clusterId) {
        this.loadClusterDetails();
        this.loadNodes();
      }
    });
  }

  loadClusterDetails(): void {
    this.loading = true;
    this.clusterService.getCluster(this.clusterId).subscribe({
      next: (data) => {
        this.cluster = data;
        this.loading = false;
      },
      error: (err) => {
        this.error = 'Failed to load cluster details.';
        this.loading = false;
        console.error('Error loading cluster:', err);
      }
    });
  }

  loadNodes(): void {
    this.nodeService.getClusterNodes(this.clusterId).subscribe({
      next: (data) => {
        this.nodes = data;
      },
      error: (err) => {
        console.error('Error loading nodes:', err);
      }
    });
  }

  deleteNode(nodeId: string, hostname: string): void {
    if (confirm(`Are you sure you want to delete node "${hostname}"?`)) {
      this.nodeService.deleteNode(this.clusterId, nodeId).subscribe({
        next: () => {
          this.loadNodes(); // Refresh nodes list
        },
        error: (err) => {
          this.error = 'Failed to delete node. Please try again.';
          console.error('Error deleting node:', err);
        }
      });
    }
  }

  viewNodeDetails(nodeId: string): void {
    this.router.navigate(['/clusters', this.clusterId, 'nodes', nodeId]);
  }

  editNode(nodeId: string): void {
    this.router.navigate(['/clusters', this.clusterId, 'nodes', nodeId, 'edit']);
  }

  addNewNode(): void {
    this.router.navigate(['/clusters', this.clusterId, 'nodes', 'new']);
  }

  editCluster(): void {
    this.router.navigate(['/clusters', this.clusterId, 'edit']);
  }

  backToList(): void {
    this.router.navigate(['/clusters']);
  }
}