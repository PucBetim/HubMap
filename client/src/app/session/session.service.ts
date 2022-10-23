import { ConfigService } from './../core/services/config.service';
import { User } from './models/user';
import { BaseService } from './../core/services/base.service';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { catchError, Observable } from 'rxjs';
import { Login } from './models/login';

@Injectable({
  providedIn: 'root'
})
export class SessionService extends BaseService {

  public userUrl = "hubmap/appUsers";

  constructor(private http: HttpClient,) { super(); }

  login(login: Login): Observable<Login> {
    return this.http.post<Login>((ConfigService.getUrlApi() + "login"), login, ConfigService.getOptions()).pipe(catchError(super.serviceError));
  };

  createUser(user: User): Observable<User> {
    return this.http.post<User>(ConfigService.getUrlApi() + this.userUrl, user, ConfigService.getOptions()).pipe(catchError(super.serviceError));
  };

  updateUser(user: User): Observable<User> {
    return this.http.put<User>(ConfigService.getUrlApi() + this.userUrl, user, ConfigService.getOptions()).pipe(catchError(super.serviceError));
  };

  getUserLogado(): Observable<any> {
    return this.http.get<any>(ConfigService.getUrlApi() + this.userUrl, ConfigService.getOptions()).pipe(catchError(super.serviceError));
  };
}
