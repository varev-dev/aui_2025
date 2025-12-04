import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ClusterListComponent } from './cluster-list';

describe('ClusterList', () => {
  let component: ClusterListComponent;
  let fixture: ComponentFixture<ClusterListComponent>;
  
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ClusterListComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ClusterListComponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
