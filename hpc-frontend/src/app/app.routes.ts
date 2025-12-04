import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ClusterListComponent } from './components/cluster-list/cluster-list';
import { ClusterFormComponent } from './components/cluster-form/cluster-form';
import { ClusterDetailsComponent } from './components/cluster-details/cluster-details';
import { NodeFormComponent } from './components/node-form/node-form';
import { NodeDetailsComponent } from './components/node-details/node-details';

export const routes: Routes = [
  // Redirect root to clusters list
  { path: '', redirectTo: '/clusters', pathMatch: 'full' },
  
  // Cluster routes
  { path: 'clusters', component: ClusterListComponent },           // Task 1: List
  { path: 'clusters/new', component: ClusterFormComponent },       // Task 2: Add new
  { path: 'clusters/:id', component: ClusterDetailsComponent },    // Task 4: Details + nodes list
  { path: 'clusters/:id/edit', component: ClusterFormComponent },  // Task 3: Edit
  
  // Node routes
  { path: 'clusters/:id/nodes/new', component: NodeFormComponent },              // Task 5: Add new node
  { path: 'clusters/:id/nodes/:nodeId', component: NodeDetailsComponent },       // Task 7: Node details
  { path: 'clusters/:id/nodes/:nodeId/edit', component: NodeFormComponent },     // Task 6: Edit node
  
  { path: '**', redirectTo: '/clusters' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})

export class AppRoutingModule { }