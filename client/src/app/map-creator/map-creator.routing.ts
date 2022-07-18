import { CreationComponent } from './creation/creation.component';
import { Routes } from '@angular/router';
import { PageNotFoundComponent } from '../core/page-not-found/page-not-found.component';


export const CreatorRoutes: Routes = [
  { path: '', component: CreationComponent },
];
