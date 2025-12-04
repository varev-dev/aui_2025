import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ClusterDetailsComponent } from './cluster-details';

describe('ClusterDetailsComponent', () => {
  let component: ClusterDetailsComponent;
  let fixture: ComponentFixture<ClusterDetailsComponent>;
  
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ClusterDetailsComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ClusterDetailsComponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
