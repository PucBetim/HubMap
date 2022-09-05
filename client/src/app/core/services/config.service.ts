
export class ConfigService {
    static urlBase: string = 'https://hub-map-server.herokuapp.com/';

    constructor() { }

    public static getUrlApi() {
        return this.urlBase;
    }

    public static getUlrAvatar() {
    }

    public static getToken() {
        let token = localStorage.getItem('hubmap.jwt');        
        if (token) return token;
        return;
    }

    public static getUser() {
        let user = JSON.parse(localStorage.getItem('hubmap.user')!);
        if(user) return user;
        return;
    }

    public static resetaLogin() {
        localStorage.clear();
        window.location.href = "/session/signin";
    }

}