import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { NodeService } from '../../services/node.service';

@Component({
  selector: 'app-node-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './node-form.html',
  styleUrl: './node-form.css'
})
export class NodeFormComponent implements OnInit {
  nodeForm: FormGroup;
  isEditMode = false;
  clusterId: string = '';
  nodeId: string | null = null;
  loading = false;
  error: string | null = null;

  constructor(
    private fb: FormBuilder,
    private nodeService: NodeService,
    private router: Router,
    private route: ActivatedRoute
  ) {
    this.nodeForm = this.fb.group({
      hostname: ['', [Validators.required, Validators.minLength(3)]],
      gpuLoad: [0, [Validators.required, Validators.min(0), Validators.max(100)]],
      temperature: [0, [Validators.required, Validators.min(0), Validators.max(150)]]
    });
  }

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      this.clusterId = params.get('id') || '';
      this.nodeId = params.get('nodeId');
      this.isEditMode = !!this.nodeId;
      
      if (this.isEditMode && this.nodeId) {
        this.loadNode(this.nodeId);
      }
    });
  }

  loadNode(nodeId: string): void {
    this.loading = true;
    this.nodeService.getNode(this.clusterId, nodeId).subscribe({
      next: (node) => {
        // Populate form with existing values
        this.nodeForm.patchValue({
          hostname: node.hostname,
          gpuLoad: node.gpuLoad,
          temperature: node.temperature
        });
        this.loading = false;
      },
      error: (err) => {
        this.error = 'Failed to load node data.';
        this.loading = false;
        console.error('Error loading node:', err);
      }
    });
  }

  onSubmit(): void {
    if (this.nodeForm.invalid) {
      Object.keys(this.nodeForm.controls).forEach(key => {
        this.nodeForm.get(key)?.markAsTouched();
      });
      return;
    }

    this.loading = true;
    this.error = null;

    const nodeData = this.nodeForm.value;

    if (this.isEditMode && this.nodeId) {
      // Update existing node
      this.nodeService.updateNode(this.clusterId, this.nodeId, nodeData).subscribe({
        next: () => {
          this.router.navigate(['/clusters', this.clusterId]);
        },
        error: (err) => {
          this.error = 'Failed to update node. Please try again.';
          this.loading = false;
          console.error('Error updating node:', err);
        }
      });
    } else {
      // Create new node
      this.nodeService.createNode(this.clusterId, nodeData).subscribe({
        next: () => {
          this.router.navigate(['/clusters', this.clusterId]);
        },
        error: (err) => {
          this.error = 'Failed to create node. Please try again.';
          this.loading = false;
          console.error('Error creating node:', err);
        }
      });
    }
  }

  cancel(): void {
    this.router.navigate(['/clusters', this.clusterId]);
  }

  isFieldInvalid(fieldName: string): boolean {
    const field = this.nodeForm.get(fieldName);
    return !!(field && field.invalid && field.touched);
  }

  getErrorMessage(fieldName: string): string {
    const field = this.nodeForm.get(fieldName);
    if (field?.errors?.['required']) {
      return `${fieldName} is required`;
    }
    if (field?.errors?.['minlength']) {
      return `${fieldName} must be at least 3 characters`;
    }
    if (field?.errors?.['min']) {
      return `${fieldName} must be at least ${field.errors['min'].min}`;
    }
    if (field?.errors?.['max']) {
      return `${fieldName} must be at most ${field.errors['max'].max}`;
    }
    return '';
  }
}