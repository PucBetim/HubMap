import { HttpHeaders } from "@angular/common/http";
import { environment } from "src/environments/environment";
import jwt_decode from "jwt-decode";

export class ConfigService {

    static urlBase: string = environment.apiUrl;

    constructor() { }

    public static getUrlApi() {
        return this.urlBase;
    }

    public static getOptions() {
        return {
            headers: new HttpHeaders({
                'Content-Type': 'application/json',
                'Authorization': this.getToken()
            }),
            observe: "response" as 'body',
        }
    }

    public static getToken() {
        let token = localStorage.getItem('hubmap.token');
        if (token) return token;
        return "";
    }

    public static getUser() {
        let user = JSON.parse(localStorage.getItem('hubmap.user')!);
        const decoded = JSON.parse(JSON.stringify(jwt_decode(this.getToken())));
        if (decoded.exp * 1000 < Date.now())
            this.resetLogin();
        else if (user) return user;
        return;
    }
    public static resetLogin() {
        localStorage.clear();
        window.location.href = "/session/signin";
    }
}