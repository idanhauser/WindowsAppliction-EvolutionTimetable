package time.table.problem.configurations.mutation;

import time.table.problem.Quintet;

import java.awt.*;
import java.io.Serializable;
import java.util.Random;

public enum EComponent implements Serializable
{
    S{
        @Override
        public void setComponent(Quintet i_quintet)
        {
            Random rn = new Random();
            i_quintet.setSubject(rn);
        }
    },

    T{
        @Override
        public void setComponent(Quintet i_quintet)
        {
            Random rn = new Random();
            i_quintet.setTeacher(rn);
        }
    },

    C
    {
        public void setComponent(Quintet i_quintet)
        {
            Random rn = new Random();
            i_quintet.setClass(rn);
        }
    },

    H{
        public void setComponent(Quintet i_quintet)
        {
            Random rn = new Random();
            i_quintet.setHour(rn);
        }
    },

    D{
        public void setComponent(Quintet i_quintet)
        {
            Random rn = new Random();
            i_quintet.setDay(rn);
        }
    };
    public abstract void setComponent(Quintet i_quintet);
    private static final long serialVersionUID = 100L;
}


