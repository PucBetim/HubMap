import { Routes } from '@angular/router';

import { MapCreatorLayoutComponent } from './core/layouts/map-creator-layout/map-creator-layout.component';
import { UserLayoutComponent } from './core/layouts/user-layout/user-layout.component';
import { PageNotFoundComponent } from './core/page-not-found/page-not-found.component';
import { MainLayoutComponent } from './core/layouts/main-layout/main-layout.component';


export const AppRoutes: Routes = [
  {
    path: 'session', component: UserLayoutComponent,
    children: [
      { path: '', loadChildren: () => import('./session/session.module').then(m => m.UserModule) },
    ]
  },
  {
    path: 'creator', component: MapCreatorLayoutComponent,
    children: [
      { path: ':id', loadChildren: () => import('./map-creator/map-creator.module').then(m => m.MapCreatorModule) },
      { path: '', loadChildren: () => import('./map-creator/map-creator.module').then(m => m.MapCreatorModule) },
    ]
  },
  {
    path: '', component: MainLayoutComponent,
    children: [
      { path: '', loadChildren: () => import('./main/main.module').then(m => m.MainModule) },
      { path: '**', component: PageNotFoundComponent }
    ]
  },

]