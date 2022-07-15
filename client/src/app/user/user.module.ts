import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { UserRoutes } from './user-module.routing';
import { RouterModule } from '@angular/router';

@NgModule({
  declarations: [],
  imports: [
    CommonModule,
    RouterModule.forChild(UserRoutes)
  ]
})
export class UserModule { }
