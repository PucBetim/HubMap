import { Injectable } from "@angular/core";
import { CanActivate, ActivatedRouteSnapshot, Router } from "@angular/router";
import { ConfigService } from "./config.service";

@Injectable()
export class AuthService implements CanActivate {
    public token: string;
    public user: any;
    public expira: number;

    constructor(private router: Router) { }

    canActivate(routeAc: ActivatedRouteSnapshot): boolean {

        return true;
    }    
}