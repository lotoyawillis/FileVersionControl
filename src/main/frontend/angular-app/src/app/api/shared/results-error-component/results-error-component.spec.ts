import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ResultsErrorComponent } from './results-error-component';

describe('ResultsErrorComponent', () => {
  let component: ResultsErrorComponent;
  let fixture: ComponentFixture<ResultsErrorComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ResultsErrorComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ResultsErrorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
