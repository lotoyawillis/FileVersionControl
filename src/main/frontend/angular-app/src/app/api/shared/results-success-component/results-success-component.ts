import {Component, OnInit} from '@angular/core';
import {ApiResponse} from '../../services/api-service';
import {Router} from '@angular/router';

@Component({
  selector: 'app-results-success-component',
  templateUrl: './results-success-component.html',
  styleUrl: './results-success-component.css',
  standalone: false
})
export class ResultsSuccessComponent implements OnInit {
  responseData?: ApiResponse;

  constructor(private router: Router) {}

  ngOnInit() {
    this.responseData = history.state.data;
  }
}
