import { Injectable } from "@angular/core";
import { CanActivate, ActivatedRouteSnapshot, Router } from "@angular/router";
import { ConfigService } from "./config.service";

@Injectable()
export class AuthService implements CanActivate {
  public user: any;

  constructor(private router: Router) { }
  canActivate(routeAc: ActivatedRouteSnapshot): boolean {
    this.user = ConfigService.getUser();
    if (this.user)
      return true;
    else {
      this.router.navigate(['/session/signin'])
      return false;
    }
  }
}