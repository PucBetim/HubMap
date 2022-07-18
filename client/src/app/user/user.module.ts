import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { UserRoutes } from './user-module.routing';
import { RouterModule } from '@angular/router';
import { CreateAccountComponent } from './create-account/create-account.component';
import { PasswordResetComponent } from './password-reset/password-reset.component';

@NgModule({
  declarations: [
    CreateAccountComponent,
    PasswordResetComponent
  ],
  imports: [
    CommonModule,
    RouterModule.forChild(UserRoutes)
  ]
})
export class UserModule { }
