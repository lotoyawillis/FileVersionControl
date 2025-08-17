import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';

export interface ApiResponse {
  status: number;
  results: string[];
  message: string | null;
}

@Injectable({
  providedIn: 'root'
})
export class ApiService {
  private apiUrl = 'http://localhost:8080/api/v1/';

  constructor(private http: HttpClient) {}

  postCommit(folderPath: String): Observable<ApiResponse> {
    this.apiUrl = 'http://localhost:8080/api/v1/commit';
    return this.http.post<ApiResponse>(this.apiUrl, folderPath);
  }

  postRestore(paths: String[]): Observable<ApiResponse> {
    this.apiUrl = 'http://localhost:8080/api/v1/restore';
    //let body: String[] = [versionFolderPath, destinationFolderPath];
    return this.http.post<ApiResponse>(this.apiUrl, paths);
  }
}
