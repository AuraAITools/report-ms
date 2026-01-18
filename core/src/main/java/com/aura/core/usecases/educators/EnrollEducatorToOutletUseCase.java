package com.aura.core.usecases.educators;

import com.aura.core.entities.outlets.Outlet;

public interface EnrollEducatorToOutletUseCase {
    Outlet enroll(String educatorId, String outletId);
}
