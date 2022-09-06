import { HttpHeaders } from "@angular/common/http";

export class ConfigService {

    static urlBase: string = 'https://hub-map-server.herokuapp.com/';

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
        let token = sessionStorage.getItem('hubmap.token');
        if (token) return token;
        return "";
    }

    public static getUser() {
        let user = JSON.parse(sessionStorage.getItem('hubmap.user')!);
        if (user) return user;
        return;
    }

    public static resetLogin() {
        sessionStorage.clear();
        window.location.href = "/session/signin";
    }
}