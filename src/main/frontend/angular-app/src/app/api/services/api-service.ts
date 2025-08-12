import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ApiService {
  private apiUrl = 'http://localhost:8080/api/v1/';

  constructor(private http: HttpClient) {}

  postCommit(folderPath: String): Observable<any> {
    this.apiUrl = this.apiUrl + 'commit';
    return this.http.post(this.apiUrl, folderPath);
  }

  postRestore(versionFolderPath: String, destinationFolderPath: String): Observable<any> {
    this.apiUrl = this.apiUrl + 'restore';
    let body: String[] = [versionFolderPath, destinationFolderPath];
    return this.http.post(this.apiUrl, body);
  }
}
