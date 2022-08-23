import { HttpErrorResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ConfigService } from './config.service';
import { throwError } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class BaseService {

  constructor() { }

  protected serviceError(error: Response | any) {
    let errMsg: string;
    if (error instanceof HttpErrorResponse) {
      if (error.error) {
        errMsg = error.error.errors ? error.error : { errors: [`${error.status} - ${error.statusText}`] };
      }
      else {
        if (error.status == 401) {
          ConfigService.resetaLogin();
        }
        errMsg = error.message ? `${error.status} - ${error.statusText}` : error.toString();
      }
    } else {
      errMsg = error.message ? error.message : error.toString();
    }
    return throwError(() => errMsg);
  }
}
