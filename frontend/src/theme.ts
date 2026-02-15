import { createTheme } from '@mui/material/styles';

export const theme = createTheme({
  palette: {
    mode: 'light',
    primary: {
      main: '#0B3D2E',
    },
    secondary: {
      main: '#F4A340',
    },
    background: {
      default: '#F7F2EB',
      paper: '#FFFFFF',
    },
  },
  typography: {
    fontFamily: '"Libre Baskerville", "Times New Roman", serif',
    h5: { fontWeight: 700 },
    h6: { fontWeight: 700 },
    button: { textTransform: 'none', fontWeight: 700 },
  },
  shape: {
    borderRadius: 16,
  },
});
