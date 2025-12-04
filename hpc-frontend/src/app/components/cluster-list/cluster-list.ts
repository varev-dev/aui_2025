import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { ClusterService } from '../../services/cluster.service';
import { ClusterList } from '../../models/cluster.model';

@Component({
  selector: 'app-cluster-list',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './cluster-list.html',
  styleUrl: './cluster-list.css'
})
export class ClusterListComponent implements OnInit {
  clusters: ClusterList[] = [];
  loading = false;
  error: string | null = null;

  constructor(
    private clusterService: ClusterService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loadClusters();
  }

  loadClusters(): void {
    this.loading = true;
    this.error = null;
    
    this.clusterService.getAllClusters().subscribe({
      next: (data) => {
        this.clusters = data;
        this.loading = false;
      },
      error: (err) => {
        this.error = 'Failed to load clusters. Please try again.';
        this.loading = false;
        console.error('Error loading clusters:', err);
      }
    });
  }

  deleteCluster(id: string, name: string): void {
    if (confirm(`Are you sure you want to delete cluster "${name}"? This will also delete all nodes in this cluster.`)) {
      this.clusterService.deleteCluster(id).subscribe({
        next: () => {
          this.loadClusters(); // Refresh list
        },
        error: (err) => {
          this.error = 'Failed to delete cluster. Please try again.';
          console.error('Error deleting cluster:', err);
        }
      });
    }
  }

  viewDetails(id: string): void {
    this.router.navigate(['/clusters', id]);
  }

  editCluster(id: string): void {
    this.router.navigate(['/clusters', id, 'edit']);
  }

  addNewCluster(): void {
    this.router.navigate(['/clusters/new']);
  }
}