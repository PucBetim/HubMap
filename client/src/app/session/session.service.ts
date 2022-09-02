import { User } from './models/user';
import { BaseService } from './../core/services/base.service';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { catchError, Observable } from 'rxjs';
import { ConfigService } from '../core/services/config.service';
import { Login } from './models/login';

@Injectable({
  providedIn: 'root'
})
export class SessionService extends BaseService {

  constructor(private http: HttpClient) { super(); }

  options = {
    headers: new HttpHeaders({
      'Content-Type': 'application/json',
    }),
    observe: "response" as 'body',
  }

  login(login: Login): Observable<Login> {
    return this.http.post<Login>((ConfigService.getUrlApi() + "login"), login, this.options).pipe(catchError(super.serviceError));
  };

  createUser(user: User): Observable<User> {
    return this.http.post<User>(ConfigService.getUrlApi() + "hubmap/appUsers", user, this.options).pipe(catchError(super.serviceError));
  };
}
