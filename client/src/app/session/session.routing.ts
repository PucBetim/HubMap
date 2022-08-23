import { PasswordResetComponent } from './password-reset/password-reset.component';
import { CreateAccountComponent } from './create-account/create-account.component';
import { PageNotFoundComponent } from '../core/page-not-found/page-not-found.component';
import { UserSettingsComponent } from './user-settings/user-settings.component';
import { LoginComponent } from './login/login.component';
import { Routes } from '@angular/router';

export const SessionRoutes: Routes = [
  { path: '', component: PageNotFoundComponent },
  { path: 'login', component: LoginComponent },
  { path: 'create-account', component: CreateAccountComponent },
  { path: 'password-reset', component: PasswordResetComponent },
  { path: 'settings/:id', component: UserSettingsComponent }
];
