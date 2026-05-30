import { createTheme, alpha } from '@mui/material/styles';

const md3Blue = {
    50: '#e8f0fe',
    100: '#c5d9fc',
    200: '#9ebcf9',
    300: '#749df5',
    400: '#5285f2',
    500: '#1a73e8',
    600: '#1557b0',
    700: '#104a91',
    800: '#0b3c73',
    900: '#062e54',
};

const theme = createTheme({
    palette: {
        mode: 'light',
        primary: {
            main: md3Blue[500],
            light: md3Blue[300],
            dark: md3Blue[700],
            contrastText: '#ffffff',
        },
        secondary: {
            main: '#5f6368',
            light: '#80868b',
            dark: '#3c4043',
        },
        error: {
            main: '#d93025',
            light: '#ea4335',
            dark: '#c5221f',
        },
        warning: {
            main: '#f9ab00',
            light: '#fbbc04',
            dark: '#e37400',
        },
        success: {
            main: '#1e8e3e',
            light: '#34a853',
            dark: '#137333',
        },
        background: {
            default: '#f8f9fa',
            paper: '#ffffff',
        },
        divider: alpha('#000000', 0.12),
    },
    typography: {
        fontFamily: '"Google Sans", "Product Sans", system-ui, -apple-system, sans-serif',
        h4: {
            fontWeight: 500,
            letterSpacing: '-0.01em',
        },
        h5: {
            fontWeight: 500,
            letterSpacing: '-0.01em',
        },
        h6: {
            fontWeight: 500,
        },
        subtitle1: {
            fontWeight: 500,
        },
        button: {
            textTransform: 'none',
            fontWeight: 500,
            letterSpacing: '0.02em',
        },
    },
    shape: {
        borderRadius: 12,
    },
    components: {
        MuiButton: {
            styleOverrides: {
                root: {
                    borderRadius: 20,
                    padding: '8px 24px',
                    fontSize: '0.875rem',
                    boxShadow: 'none',
                    '&:hover': {
                        boxShadow: 'none',
                    },
                },
                contained: {
                    '&:hover': {
                        boxShadow: `0 1px 3px 1px ${alpha(md3Blue[500], 0.15)}, 0 1px 2px ${alpha(md3Blue[500], 0.3)}`,
                    },
                },
                outlined: {
                    borderWidth: 1,
                    '&:hover': {
                        borderWidth: 1,
                    },
                },
            },
        },
        MuiTextField: {
            styleOverrides: {
                root: {
                    '& .MuiOutlinedInput-root': {
                        borderRadius: 12,
                        backgroundColor: '#ffffff',
                        '&:hover .MuiOutlinedInput-notchedOutline': {
                            borderColor: md3Blue[300],
                        },
                        '&.Mui-focused .MuiOutlinedInput-notchedOutline': {
                            borderColor: md3Blue[500],
                            borderWidth: 2,
                        },
                    },
                },
            },
        },
        MuiPaper: {
            styleOverrides: {
                root: {
                    borderRadius: 16,
                    backgroundImage: 'none',
                },
                elevation0: {
                    boxShadow: 'none',
                },
                elevation1: {
                    boxShadow: '0 1px 2px 0 rgba(60, 64, 67, 0.3), 0 1px 3px 1px rgba(60, 64, 67, 0.15)',
                },
                elevation2: {
                    boxShadow: '0 1px 3px 0 rgba(60, 64, 67, 0.3), 0 4px 8px 3px rgba(60, 64, 67, 0.15)',
                },
            },
        },
        MuiCard: {
            styleOverrides: {
                root: {
                    borderRadius: 20,
                    boxShadow: '0 1px 3px 0 rgba(60, 64, 67, 0.3), 0 4px 8px 3px rgba(60, 64, 67, 0.15)',
                },
            },
        },
        MuiAppBar: {
            styleOverrides: {
                root: {
                    boxShadow: '0 1px 2px 0 rgba(60, 64, 67, 0.3), 0 1px 3px 1px rgba(60, 64, 67, 0.15)',
                    backgroundImage: 'none',
                },
            },
        },
        MuiChip: {
            styleOverrides: {
                root: {
                    borderRadius: 8,
                    fontWeight: 500,
                },
            },
        },
        MuiTableHead: {
            styleOverrides: {
                root: {
                    '& .MuiTableCell-head': {
                        fontWeight: 600,
                        color: '#5f6368',
                        borderBottom: '1px solid rgba(0,0,0,0.08)',
                    },
                },
            },
        },
        MuiTableCell: {
            styleOverrides: {
                root: {
                    borderBottom: '1px solid rgba(0,0,0,0.06)',
                    padding: '14px 16px',
                },
            },
        },
        MuiIconButton: {
            styleOverrides: {
                root: {
                    borderRadius: 12,
                },
            },
        },
        MuiAlert: {
            styleOverrides: {
                root: {
                    borderRadius: 12,
                },
            },
        },
    },
});

export default theme;
