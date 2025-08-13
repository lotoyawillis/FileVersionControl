import {Component, Injectable} from '@angular/core';
import {ApiService} from '../../services/api-service';

@Component({
  selector: 'app-commit-component',
  // imports: [],
  templateUrl: './commit-component.html',
  styleUrl: './commit-component.css',
  standalone: false
})
@Injectable({
  providedIn: 'root'
})
export class CommitComponent {
  constructor(private apiService: ApiService) {
  }

  submit(directory: String) {
    this.apiService.postCommit(directory).subscribe(
      (response) => {
        console.log('API Response:', response);
      },
      (error) => {
        console.error('API Error:', error);
      }
    );
  }

  ngOnInit() {}
}
