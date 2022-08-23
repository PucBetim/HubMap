
export class ConfigService {
    static urlBase: string = 'https://hub-map-server.herokuapp.com/';

    constructor() { }

    public static getUrlApi() {
        return this.urlBase;
    }

    public static getUlrAvatar() {
    }

    public static getToken() {

    }

    public static getUser() {

    }

    static getDataExpira() {
    }

    public static resetaLogin() {
        localStorage.clear();
        window.location.href = "/session/signin";
    }

}