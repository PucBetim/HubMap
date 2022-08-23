import { LoginComponent } from './login/login.component';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';

import { SessionRoutes } from './session.routing';
import { CreateAccountComponent } from './create-account/create-account.component';
import { PasswordResetComponent } from './password-reset/password-reset.component';
import { SessionService } from './session.service';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

@NgModule({
  declarations: [
    CreateAccountComponent,
    PasswordResetComponent,
    LoginComponent
  ],
  imports: [
    CommonModule,
    RouterModule.forChild(SessionRoutes),
    ReactiveFormsModule,
    FormsModule,
    MatFormFieldModule,
    MatInputModule

  ],
  providers:[
    SessionService
  ]
})
export class UserModule { }
