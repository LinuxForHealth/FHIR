/*
 * (C) Copyright IBM Corp. 2019, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.model.visitor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Year;
import java.time.YearMonth;
import java.time.ZonedDateTime;
import java.time.temporal.TemporalAccessor;
import java.util.List;

import com.ibm.fhir.model.util.FHIRUtil;

public abstract class AbstractVisitable implements Visitable {
    @Override
    public abstract void accept(String elementName, int elementIndex, Visitor visitor);

    protected void accept(Visitable visitable, java.lang.String elementName, Visitor visitor) {
        if (visitable != null) {
            visitable.accept(elementName, -1, visitor);
        }
    }

    protected void accept(List<? extends Visitable> visitables, java.lang.String elementName, Visitor visitor, Class<?> type) {
        visitor.visitStart(elementName, visitables, type);
        if (!visitables.isEmpty()) {
            int elementIndex = 0;
            for (Visitable visitable : visitables) {
                visitable.accept(elementName, elementIndex, visitor);
                elementIndex++;
            }
        }
        visitor.visitEnd(elementName, visitables, type);
    }
    
    protected void accept(BigDecimal value, java.lang.String elementName, Visitor visitor) {
        if (value != null) {
            visitor.visit(elementName, value);
        }
    }
    
    protected void accept(byte[] value, java.lang.String elementName, Visitor visitor) {
        if (value != null) {
            visitor.visit(elementName, value);
        }
    }
    
    protected void accept(java.lang.Integer value, java.lang.String elementName, Visitor visitor) {
        if (value != null) {
            visitor.visit(elementName, value);
        }
    }

    protected void accept(LocalDate value, java.lang.String elementName, Visitor visitor) {
        if (value != null) {
            visitor.visit(elementName, value);
        }
    }
    
    protected void accept(LocalTime value, java.lang.String elementName, Visitor visitor) {
        if (value != null) {
            visitor.visit(elementName, value);
        }
    }
    
    protected void accept(java.lang.String value, java.lang.String elementName, Visitor visitor) {
        if (value != null) {
            visitor.visit(elementName, value);
        }
    }
    
    protected void accept(java.lang.Boolean value, java.lang.String elementName, Visitor visitor) {
        if (value != null) {
            visitor.visit(elementName, value);
        }
    }
    
    protected void accept(Year value, java.lang.String elementName, Visitor visitor) {
        if (value != null) {
            visitor.visit(elementName, value);
        }
    }
    
    protected void accept(YearMonth value, java.lang.String elementName, Visitor visitor) {
        if (value != null) {
            visitor.visit(elementName, value);
        }
    }
    
    protected void accept(ZonedDateTime value, java.lang.String elementName, Visitor visitor) {
        if (value != null) {
            visitor.visit(elementName, value);
        }
    }
    
    // for Date and DateTime
    protected void accept(TemporalAccessor temporal, java.lang.String elementName, Visitor visitor) {
        if (temporal instanceof ZonedDateTime) {
            visitor.visit(elementName, (ZonedDateTime) temporal);
        } else if (temporal instanceof LocalDate) {
            visitor.visit(elementName, (LocalDate) temporal);
        } else if (temporal instanceof YearMonth) {
            visitor.visit(elementName, (YearMonth) temporal);
        } else if (temporal instanceof Year) {
            visitor.visit(elementName, (Year) temporal);
        }
    }
    
    @Override
    public String toString() {
        return FHIRUtil.toString(this);
    }
}
