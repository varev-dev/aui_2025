import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NodeFormComponent } from './node-form';

describe('NodeForm', () => {
  let component: NodeFormComponent;
  let fixture: ComponentFixture<NodeFormComponent>;
  
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [NodeFormComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(NodeFormComponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
