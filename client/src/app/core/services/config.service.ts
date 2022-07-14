
export class ConfigService {
    static urlBase: string = 'notUrl';
    static versaoLocal: string = 'notVersao';

    constructor() { }

    public static getUrlApi() {
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