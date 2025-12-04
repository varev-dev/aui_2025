import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { NodeService } from '../../services/node.service';
import { Node } from '../../models/node.model';

@Component({
  selector: 'app-node-details',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './node-details.html',
  styleUrl: './node-details.css'
})
export class NodeDetailsComponent implements OnInit {
  node: Node | null = null;
  clusterId: string = '';
  nodeId: string = '';
  loading = false;
  error: string | null = null;

  constructor(
    private nodeService: NodeService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      this.clusterId = params.get('id') || '';
      this.nodeId = params.get('nodeId') || '';
      
      if (this.clusterId && this.nodeId) {
        this.loadNodeDetails();
      }
    });
  }

  loadNodeDetails(): void {
    this.loading = true;
    this.error = null;
    
    this.nodeService.getNode(this.clusterId, this.nodeId).subscribe({
      next: (data) => {
        this.node = data;
        this.loading = false;
      },
      error: (err) => {
        this.error = 'Failed to load node details.';
        this.loading = false;
        console.error('Error loading node:', err);
      }
    });
  }

  editNode(): void {
    this.router.navigate(['/clusters', this.clusterId, 'nodes', this.nodeId, 'edit']);
  }

  deleteNode(): void {
    if (this.node && confirm(`Are you sure you want to delete node "${this.node.hostname}"?`)) {
      this.nodeService.deleteNode(this.clusterId, this.nodeId).subscribe({
        next: () => {
          this.router.navigate(['/clusters', this.clusterId]);
        },
        error: (err) => {
          this.error = 'Failed to delete node. Please try again.';
          console.error('Error deleting node:', err);
        }
      });
    }
  }

  backToCluster(): void {
    this.router.navigate(['/clusters', this.clusterId]);
  }

  getLoadStatus(gpuLoad: number): string {
    if (gpuLoad > 80) return 'high';
    if (gpuLoad > 60) return 'medium';
    return 'low';
  }

  getTemperatureStatus(temperature: number): string {
    if (temperature > 75) return 'high';
    if (temperature > 65) return 'medium';
    return 'normal';
  }
}