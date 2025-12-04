import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ClusterFormComponent } from './cluster-form';

describe('ClusterForm', () => {
  let component: ClusterFormComponent;
  let fixture: ComponentFixture<ClusterFormComponent>;
  
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ClusterFormComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ClusterFormComponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
