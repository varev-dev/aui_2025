import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NodeDetails } from './node-details';

describe('NodeDetails', () => {
  let component: NodeDetails;
  let fixture: ComponentFixture<NodeDetails>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [NodeDetails]
    })
    .compileComponents();

    fixture = TestBed.createComponent(NodeDetails);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
