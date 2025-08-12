import {Component, inject} from '@angular/core';
import {ApiService} from '../../api/services/api-service';
import {Router, RouterOutlet} from '@angular/router';

@Component({
  selector: 'app-homepage-component',
  // imports: [],
  templateUrl: './homepage-component.html',
  styleUrl: './homepage-component.css',
  standalone: false
})
export class HomepageComponent {
  constructor(private apiService: ApiService, private router: Router) {
    this.router = inject(Router);
  }

  callCommit() {
    let promise: Promise<boolean> = this.router.navigateByUrl('commit');
  }

  callRestore() {
    let promise: Promise<boolean> = this.router.navigateByUrl('restore');
  }
}
