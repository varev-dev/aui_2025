import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { ClusterService } from '../../services/cluster.service';

@Component({
  selector: 'app-cluster-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './cluster-form.html',
  styleUrl: './cluster-form.css'
})
export class ClusterFormComponent implements OnInit {
  clusterForm: FormGroup;
  isEditMode = false;
  clusterId: string | null = null;
  loading = false;
  error: string | null = null;

  constructor(
    private fb: FormBuilder,
    private clusterService: ClusterService,
    private router: Router,
    private route: ActivatedRoute
  ) {
    this.clusterForm = this.fb.group({
      name: ['', [Validators.required, Validators.minLength(3)]],
      location: ['', [Validators.required, Validators.minLength(3)]]
    });
  }

  ngOnInit(): void {
    // Check if we're in edit mode (clusterId in route params)
    this.route.paramMap.subscribe(params => {
      this.clusterId = params.get('id');
      this.isEditMode = !!this.clusterId;
      
      if (this.isEditMode && this.clusterId) {
        this.loadCluster(this.clusterId);
      }
    });
  }

  loadCluster(id: string): void {
    this.loading = true;
    this.clusterService.getCluster(id).subscribe({
      next: (cluster) => {
        // Populate form with existing values
        this.clusterForm.patchValue({
          name: cluster.name,
          location: cluster.location
        });
        this.loading = false;
      },
      error: (err) => {
        this.error = 'Failed to load cluster data.';
        this.loading = false;
        console.error('Error loading cluster:', err);
      }
    });
  }

  onSubmit(): void {
    if (this.clusterForm.invalid) {
      Object.keys(this.clusterForm.controls).forEach(key => {
        this.clusterForm.get(key)?.markAsTouched();
      });
      return;
    }

    this.loading = true;
    this.error = null;

    const clusterData = this.clusterForm.value;

    if (this.isEditMode && this.clusterId) {
      // Update existing cluster
      this.clusterService.updateCluster(this.clusterId, clusterData).subscribe({
        next: () => {
          this.router.navigate(['/clusters']);
        },
        error: (err) => {
          this.error = 'Failed to update cluster. Please try again.';
          this.loading = false;
          console.error('Error updating cluster:', err);
        }
      });
    } else {
      // Create new cluster
      this.clusterService.createCluster(clusterData).subscribe({
        next: () => {
          this.router.navigate(['/clusters']);
        },
        error: (err) => {
          this.error = 'Failed to create cluster. Please try again.';
          this.loading = false;
          console.error('Error creating cluster:', err);
        }
      });
    }
  }

  cancel(): void {
    this.router.navigate(['/clusters']);
  }

  // Helper methods for validation
  isFieldInvalid(fieldName: string): boolean {
    const field = this.clusterForm.get(fieldName);
    return !!(field && field.invalid && field.touched);
  }

  getErrorMessage(fieldName: string): string {
    const field = this.clusterForm.get(fieldName);
    if (field?.errors?.['required']) {
      return `${fieldName} is required`;
    }
    if (field?.errors?.['minlength']) {
      return `${fieldName} must be at least 3 characters`;
    }
    return '';
  }
}