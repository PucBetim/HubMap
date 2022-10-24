import { ConfigService } from './../../services/config.service';
import { Router } from '@angular/router';
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-main-layout',
  templateUrl: './main-layout.component.html',
  styleUrls: ['./main-layout.component.scss']
})
export class MainLayoutComponent implements OnInit {

  public user: any;

  constructor(public router: Router) { }

  ngOnInit(): void {
    this.user = ConfigService.getUser();
  }

  configUser() {
    this.router.navigate(["/session/settings"])
  }

  logout() {
    sessionStorage.clear();
    this.user = null;
    this.router.navigate(['']);
  }
}
