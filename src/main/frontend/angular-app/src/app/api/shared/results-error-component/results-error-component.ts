import {Component, OnInit} from '@angular/core';
import {ApiResponse} from '../../services/api-service';
import {Router} from '@angular/router';

@Component({
  selector: 'app-results-error-component',
  templateUrl: './results-error-component.html',
  styleUrl: './results-error-component.css',
  standalone: false
})
export class ResultsErrorComponent implements OnInit {
  responseData?: ApiResponse;

  constructor(private router: Router) {}

  ngOnInit() {
    this.responseData = history.state.data;
  }
}
