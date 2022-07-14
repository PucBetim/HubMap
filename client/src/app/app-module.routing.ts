import { Routes } from '@angular/router';
import { HomeComponent } from './core/home-layout/home.component';

export const AppRoutes: Routes = [
  {
    path: '', component: HomeComponent,
    children: [
      { path: '', loadChildren: () => import('./core/menu/menu.module').then(m => m.MenuModule) },
    ]
  }
]
