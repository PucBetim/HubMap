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
        // this.token = ConfigService.getToken();
        // this.user = ConfigService.getUser();
        // this.expira = ConfigService.getDataExpira();  
        // if (!this.token) {            
        //     this.router.navigate(['/session/signin'])
        //     return false;
        // }

        // if(this.expira < Date.now()) {
        //     localStorage.clear();
        //     this.router.navigate(['/session/signin'])
        //     return false;
        // }
        
        // let claim: any = routeAc.data[0];
        // if (claim !== undefined) {
        //     let claim = routeAc.data[0]['claim'];

        //     if (claim) {
        //         if (!this.user.claims) {
        //             this.router.navigate(['/acesso-negado']);
        //             return false;
        //         }

        //         let userClaims = this.user.claims.some(x => x.type === claim.nome && x.value === claim.valor);
        //         if (!userClaims) {
        //             this.router.navigate(['/acesso-negado']);
        //             return false;
        //         }
        //     }
        // }

        // Descomentar quando for criar autenticação

        return true;
    }    
}