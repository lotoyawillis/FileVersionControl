import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ResultsSuccessComponent } from './results-success-component';

describe('ResultsSuccessComponent', () => {
  let component: ResultsSuccessComponent;
  let fixture: ComponentFixture<ResultsSuccessComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ResultsSuccessComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ResultsSuccessComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
