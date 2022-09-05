import { Router } from '@angular/router';
import { User } from './../../../session/models/user';
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-main-layout',
  templateUrl: './main-layout.component.html',
  styleUrls: ['./main-layout.component.scss']
})
export class MainLayoutComponent implements OnInit {

  public user: any;
  constructor(private router: Router) { }

  ngOnInit(): void {
    this.user = JSON.parse(sessionStorage.getItem('hubmap.user')!);
    console.log(this.user)
  }

  configUser(){
    this.router.navigate(["/session/settings"])
  }

  logout() {
    sessionStorage.clear();
    this.user = null;
  }
}
